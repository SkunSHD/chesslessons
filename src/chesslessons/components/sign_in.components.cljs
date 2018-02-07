(ns chesslessons.components.sign_in.components
	(:require
		[chesslessons.firebase :as fbs]
		;       Models
		[chesslessons.sign_in-model :as sign_in-model]
))


(def log (.-log js/console))


(defn render []
	(log @sign_in-model/is_button_visible "is_button_visible")
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

	 [:button.btn.btn-primary {:on-click sign_in-model/toggle} "toggle"]

	 [:div.form-group {:style {:display (if @sign_in-model/is_button_visible "block" "none")}}
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png" :onClick fbs/facebook_auth :style {:cursor "pointer"} }]
	  [:img {:src "https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Google_base_44dp.png" :onClick fbs/google_auth :style {:cursor "pointer" :height 60} }]
	  ]
	 ]
	)
