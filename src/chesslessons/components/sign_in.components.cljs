(ns chesslessons.components.sign_in.components
	(:require
		[chesslessons.firebase :as fbs :refer [firebase]]
))


(defn render []
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
	 
	 [:div.form-group
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png" :onClick fbs/facebook_auth :style {:cursor "pointer"} }]
	  ]
	 
	 [:button.btn.btn-primary "Sign in"]
	 ])