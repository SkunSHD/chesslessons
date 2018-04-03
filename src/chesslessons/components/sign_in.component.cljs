(ns chesslessons.components.sign_in.component
	(:require
		[chesslessons.firebase :as fbs]
		[chesslessons.firebase.db :as db]
		[reagent.core :refer [atom]]
; 		Components
		[chesslessons.components.sign_in_anonymous.component :refer [render_anonymous_auth]]
;       Models
		[chesslessons.visitor-model :as visitor_model]
; 		Utils
		[clojure.string :as srting]
		))


(def log (.-log js/console))


;; ==================
;; Atoms
(defonce toggle (atom ""))


(defn- -toggle_button [event group_name]
	(.preventDefault event)
	(reset! toggle (if (= group_name @toggle) "" group_name)))


; ==================
; Privat
(defn- -on_textarea_change [event]
	(visitor_model/set_visitor_message (.-value (.-currentTarget event)))
	)


(defn render_social_network_auth []
	[:div {:style {:display (if (= @toggle "social_nertwork" ) "block" "none")}}
	 [:div.form-group
	  [:img {:src "https://i.stack.imgur.com/ZW4QC.png"
			 :onClick #(fbs/facebook_auth visitor_model/set_visitor)
			 :style {:cursor "pointer"} }]
	  [:img {:src "https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Google_base_44dp.png"
			 :onClick #(fbs/google_auth visitor_model/set_visitor)
			 :style {:cursor "pointer" :height 60} }]
	  [:div.input-group
	   [:div.input-group-prepend
		[:span.input-group-text "Message"]]
	   [:textarea.form-control {:on-change -on_textarea_change
							   :value @visitor_model/visitor_message
							   :placeholder "Write here your question"}]]
	  ]
	 ])


(defn render []
	[:div.row.justify-content-md-center
	 [:div.col.col-lg-6
	  [:form
	   [:p.h1 "Chess Lessons"]
	   [:p.h3 "Looking for private in-home or in-studio Chess lessons? Coach is ready to get you started. Let's start today!"]
	   [:button.btn.btn-success {:on-click #(-toggle_button % "social_nertwork")
								 :class (if (= @toggle "social_nertwork") "active" "")} "Leave my social network contacts"]
	   [:button.btn.btn-success {:on-click #(-toggle_button % "anonymous")
								 :class (if (= @toggle "anonymous") "active" "")} "Call me back"]
	   [render_social_network_auth]
	   [render_anonymous_auth @toggle]
	   ]
	  ]
	 ]
	)
