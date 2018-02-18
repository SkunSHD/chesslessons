(ns chesslessons.components.sign_in.components
	(:require
		[chesslessons.firebase :as fbs]
;       Models
		[chesslessons.sign_in-model :as sign_in-model]
		[chesslessons.user-model :as user_model]
))


(def log (.-log js/console))


(defn render []
	[:form
	 [:p.h1 "Chess Lessons"]
	 [:p.h3 "Looking for private in-home or in-studio Chess lessons? Coach is ready to get you started. Let's start today!"]

	 [:button.btn.btn-primary {:on-click sign_in-model/toggle} "Get in touch with me"]

	 [:div.form-group {:style {:display (if @sign_in-model/is_button_visible "block" "none")}}
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png"
	         :onClick #(fbs/facebook_auth user_model/set_user)
	         :style {:cursor "pointer"} }]
	  [:img {:src "https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Google_base_44dp.png"
	         :onClick #(fbs/google_auth user_model/set_user)
	         :style {:cursor "pointer" :height 60} }]
	  ]
	 ]
	)
