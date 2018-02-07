(ns chesslessons.components.admin.components
	(:require
		[chesslessons.firebase :as fbs]
		[reagent.core :refer [atom cursor]]
;		Models
		[chesslessons.admin-model :as admin_model]
))


(def log (.-log js/console))


; ==================
; Atoms
(defonce email (atom ""))
(defonce password (atom ""))



; ==================
; Private
(defn- -email_on_change [e]
	(admin_model/set_sign_in_error_msg "")
	(reset! email (.-value (.-target e))))


(defn- -password_on_change [e]
	(admin_model/set_sign_in_error_msg "")
	(reset! password (.-value (.-target e))))


(defn- -on_submit_success [e]
	(.preventDefault e)
	(admin_model/sign_in_admin @email @password)
	)

(defn- -on_submit_press [e]
	(if (or (empty? @email) (empty? @password))
		(log "Invalid")
		(-on_submit_success e)
		)
	)


; ==================
; Components
(defn render_login_form []
	[:form
	 [:h1.h3.mb-3.font-weight-normal "Admin sign in"]
	
	 [:div.form-group
	  [:label.sr-only "Email address"]
	  [:input.form-control {:placeholder "Email address"
	                        :required true
	                        :on-change -email_on_change
	                        :type "email"}]
	  ]
	
	 [:div.form-group
	  [:label.sr-only "Password"]
	  [:input.form-control {:placeholder "Password"
	                        :required true
	                        :on-change -password_on_change
	                        :type "password"}]
	  ]
	
	 [:div.form-group
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png" :onClick admin_model/admin_facebook_auth :style {:cursor "pointer"} }]
	  ]
	 
	 (if-not (empty? @admin_model/sign_in_error_msg) [:p @admin_model/sign_in_error_msg])
	
	 [:button.btn.btn-primary {:on-click -on_submit_press} "Sign in" ]
	 ])


(defn render_admin_container []
	(log "admin: " @admin_model/admin)
	[:div "admin container"])