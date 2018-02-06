(ns chesslessons.components.sign_in.components
	(:require
		[chesslessons.firebase :as fbs]
))


(def log (.-log js/console))


; ==================
; Atoms
(def is_button_visible (atom nil))


(defn toggle [e]
	(.preventDefault e)
	(reset! is_button_visible true)
	(log "click3" @is_button_visible)
	)


(defn render []
	(log @is_button_visible "is_button_visible")
	[:form
	 [:h1.h3.mb-3.font-weight-normal "Please sign in"]
	 
	 [:div.form-group
	  [:label.sr-only "Email address"]
	  [:input.form-control {:placeholder "Email address" :required true :type "email"}]
	  ]
	
	 [:div.form-group
	  [:label.sr-only "Password"]
	  [:input.form-control {:placeholder "Password" :required true :type "password"}]
	  ]
	 
	 [:div.form-group {:style {:display (if @is_button_visible "block" "none")}}
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png" :onClick fbs/facebook_auth :style {:cursor "pointer"} }]
	  ]
	 
	 [:button.btn.btn-primary {:on-click toggle} "Sign in"]
	 ])