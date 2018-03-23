(ns chesslessons.components.sign_in.component
	(:require
		[chesslessons.firebase :as fbs]
		[chesslessons.firebase.db :as db]
		[reagent.core :refer [atom]]
;       Models
		[chesslessons.visitor-model :as visitor_model]
; 		Utils
		[clojure.string :as srting]
		))


(def log (.-log js/console))

;; ==================
;; Atoms
(defonce visitor_info (atom {:phone "" :message ""}))
(defonce toggle (atom ""))


(defn- -toggle_button [event group_name]
	(.preventDefault event)

	(reset! toggle (if (= group_name @toggle) "" group_name)))


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

(defn- sign_in_anonimously []
	(log "1 sign_in_anonimously")
	(.signInAnonymously (fbs/auth)))

(defn- -save_anonimous_info []
	(log "2 save_anonimous_info")
	(db/save_anonymous_message (js/parseInt (:phone @visitor_info)) (:message @visitor_info))
	)


(defn- on_visitor_info_change [info_type_name]
	(fn [event]
		(swap! visitor_info assoc  info_type_name (.-value (.-currentTarget event))))
	)


(defn- on_call_back_handle [e]
	(.preventDefault e)
	(.then (sign_in_anonimously)
				 (fn []
					 (.then (-save_anonimous_info) (fn []
																					 (fbs/delete_current_visitor_from_firebase) )))
				 ))


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
	   [:textarea.form-control {:on-change on_textarea_change
							   :value @visitor_model/visitor_message
							   :placeholder "Write here your question"}]]
	  ]
	 ])


(defn render_anonymous_auth []
	[:div {:style {:display (if (= @toggle "anonymous" ) "block" "none")}}
	 [:div.input-group
	  [:div.input-group-prepend
	   [:span.input-group-text {:style {:padding-left 48}} "+38"]]
	  [:input.form-control {:value (:phone @visitor_info)
							:on-change (on_visitor_info_change :phone)
							:type "text"
							:aria-label "Telephone number"
							:placeholder "Phone number"}]
	  ]
	 [:div.input-group
	  [:div.input-group-prepend
	   [:span.input-group-text "Message"]]

	  [:textarea.form-control
	   {:on-change   (on_visitor_info_change :message)
		:value       (:message @visitor_info)
		:placeholder "Send me a question"
		:aria-label  "Leave a message"}]]
	 [:button.btn.btn-warning {:on-click on_call_back_handle} "Send info"]
	 ]
	)


(defn render []
	(log @toggle)
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
	   [render_anonymous_auth]
	   ]
	  ]
	 ]
	)
