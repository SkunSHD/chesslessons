(ns chesslessons.components.admin.components
	(:require
		[chesslessons.firebase :as fbs]
		[reagent.core :refer [atom cursor]]
;		Models
		[chesslessons.admin-model :as admin_model]
        [chesslessons.visitors-model :as visitors_model]
;       Utils
		[chesslessons.firebase.db :as db]
;		Components
		[chesslessons.components.admin.admin-form.components :as admin_form_components]
))


(def log (.-log js/console))


; ==================
; Atoms
(defonce email (atom ""))
(defonce password (atom ""))




; ==================
; Private
(defn- -email_on_change [e]
	(if-not (empty? @admin_model/sign_in_error_msg) (admin_model/set_sign_in_error_msg ""))
	(reset! email (.-value (.-target e))))


(defn- -password_on_change [e]
	(if @admin_model/sign_in_error_msg (admin_model/set_sign_in_error_msg ""))
	(reset! password (.-value (.-target e))))


(defn- -on_submit_success [e]
	(.preventDefault e)
	(admin_model/sign_in_admin @email @password)
	)

(defn- -on_submit_press [e]
	(if (or (empty? @email) (empty? @password))
		(log "Invalid")
		(-on_submit_success e)
		))



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

	 (if-not (empty? @admin_model/sign_in_error_msg) [:p @admin_model/sign_in_error_msg])

	 [:button.btn.btn-primary {:on-click -on_submit_press} "Sign in" ]
	 ])


(defn render_admin_visitor_img_src [visitor]
	(cond
		(:facebook_link visitor) "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/facebook_square-48.png"
		(:google_link visitor) "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/google_circle_color-48.png"
		:else nil)
		)


(defn render_admin_visitor_link [visitor]
	(if (or (:google_link visitor) (:facebook_link visitor))
		[:a {:href (or (:google_link visitor) (:facebook_link visitor)) :cursor "pointer" :target "_blank"}
			[:img {:src (render_admin_visitor_img_src visitor)}]]
		))


(defn render_admin_visitor [visitor]
	[:li {:key (:uid visitor)}
		 [:img {:src (:photo visitor) :width 50 :height 50}]
		 [:p "email: " (:email visitor)]
		 [:p "name: " (:name visitor)]
		 [render_admin_visitor_link visitor]
		 [:hr]
	 ])


(defn render_admin_visitors []
	[:ul {:style {:text-align "left" :list-style "none"}}
	 (for [visitor @visitors_model/visitors]
		 ^{:key visitor} (render_admin_visitor visitor))])


(defn render_admin_container []
	 [:div
	   	[:h1 "admin container"]
	    [admin_form_components/render_admin_form]
	    [render_admin_visitors]])