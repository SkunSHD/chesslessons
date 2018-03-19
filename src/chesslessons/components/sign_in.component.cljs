(ns chesslessons.components.sign_in.component
	(:require
		[chesslessons.firebase :as fbs]
		[reagent.core :refer [atom]]
;       Models
		[chesslessons.visitor-model :as visitor_model]
; 		Utils
		[clojure.string :as srting]
		))


(def log (.-log js/console))

;; ==================
;; Atoms
(defonce toggle (atom nil))


(defn- -toggle_button [event]
	(.preventDefault event)
	(reset! toggle (not @toggle)))


; ==================
; Privat
(defn- validate [text]
	(let [valid_text (if (> (count text) 3000)
						 (subs text 0 3000)
						 text)]
		(srting/trim valid_text))
	)


(defn- on_textarea_change [event]
	(visitor_model/set_visitor_message (.-value (.-currentTarget event)))
	)


(defn render []
	[:form
	 [:p.h1 "Chess Lessons"]
	 [:p.h3 "Looking for private in-home or in-studio Chess lessons? Coach is ready to get you started. Let's start today!"]
	 [:button.btn.btn-primary {:on-click -toggle_button} "Get in touch with me"]

	 [:div.form-group {:style {:display (if @toggle "block" "none")}}
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png"
	         :onClick #(fbs/facebook_auth visitor_model/set_visitor)
	         :style {:cursor "pointer"} }]
	  [:img {:src "https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Google_base_44dp.png"
	         :onClick #(fbs/google_auth visitor_model/set_visitor)
	         :style {:cursor "pointer" :height 60} }]
	  [:div
	   [:textarea {:on-change on_textarea_change
				   :value @visitor_model/visitor_message
				   :rows 4 :cols 45
				   :placeholder "Write here your question or phone number if you want me to call you back. Don't forget to enter via social network afterwords!"}]]
	  ]
	 ]
	)
