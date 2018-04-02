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
(defonce visitor_info (atom {:phone "" :message "" :error {}}))
(defonce toggle (atom ""))


(defn- -toggle_button [event group_name]
	(.preventDefault event)
	(reset! toggle (if (= group_name @toggle) "" group_name)))


; ==================
; Privat
(defn- -on_textarea_change [event]
	(visitor_model/set_visitor_message (.-value (.-currentTarget event)))
	)


(defn- -sign_in_anonimously []
	(.signInAnonymously (fbs/auth)))


(defn- -save_anonimous_info []
	(db/save_anonymous_message (js/parseInt (:phone @visitor_info)) (:message @visitor_info))
	)


; Form Validation
(defn- -required? [field_name]
	(let [required_fields '(:phone)]
		(some #(= field_name %) required_fields)
		)
	)


(defn- -is_tel_wrote? []
	(not (empty? (:phone @visitor_info)))
	)


(defn- -set_visitor_info_error [field_name & [message]]
	(let [error_message (cond
							(= message "") ""
							(> (count message) 0) message
							(nil? message) (case field_name
											   :phone (if (empty? (field_name @visitor_info))
														  "Phone field is required"
														  "Type just numbers please")
											   :message "The message is too long (1000 characters is max)"))]
		(swap! visitor_info assoc-in [:error field_name] error_message)))


(defn- -clear_field_error_message[field_name]
	(-set_visitor_info_error field_name "")
	)


(defn- -error_exist? [field_name]
	(not (empty? (field_name (:error @visitor_info))))
	)


(defn not_number_or_space? [field_value]
	(re-find #"[^0-9\s]" field_value))


(defn- -validate [field_name field_value]
	(case field_name
			:phone (cond
					   (not_number_or_space? field_value) {:valid? false :error_message "Type just numbers please"}
					   :else  {:valid? true}
					   )
			:message (if (< (count field_value) 1000)
						 {:valid? true}
						 {:valid? false :error_message "The message is too long (1000 characters is max)"}
						 )))


(defn- on_visitor_info_change [field_name]
	 (fn [event]
		 (let [new_field_value (.-value (.-currentTarget event))]
			;clear phone message
			 (when
				(and
				 (-error_exist? field_name)
					(not (empty? new_field_value)))
				(-clear_field_error_message field_name))

			 (let [{valid? :valid? error_message :error_message} (-validate field_name new_field_value)]
				 (log "valid " valid?)
				 (log "field_name " field_name "new_field_value " new_field_value)
				 (if valid?
					 (swap! visitor_info assoc field_name new_field_value)
					 (-set_visitor_info_error field_name error_message)
					 )
				 )
			)
		)
	)


(defn- on_post_anonymous_info_handle [e]
	(.preventDefault e)
	(if (-is_tel_wrote?)
		(.then (-sign_in_anonimously) (fn []
										  (.then (-save_anonimous_info) (fn []
																			(fbs/delete_current_visitor_from_firebase))))
			   )
		(-set_visitor_info_error :phone "Phone field is required")
		)
	)


(defn render_error_message []
	(let [error_object (:error @visitor_info)]
		(if (not (empty? error_object))
			[:div
			 (for [[key value] error_object]
				 [:p
				  {:key   key
				   :style {:color      "darkRed"
						   :fontWeight "bold"}}
				  (key error_object)])])))


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


(defn render_anonymous_auth []
	[:div {:style {:display (if (= @toggle "anonymous" ) "block" "none")}}
	 [:div.input-group
	  [:div.input-group-prepend
	   [:span.input-group-text {:style {:padding-left 48}} "+38"]]
	  [:input.form-control {:value (:phone @visitor_info)
							:on-change (on_visitor_info_change :phone)
							:style {:border-color (when (-error_exist? :phone) "red")}
							:type "text"
							:aria-label "Telephone number"
							:placeholder "Phone number"}]
	  ]
	 [:div.input-group
	  [:div.input-group-prepend
	   [:span.input-group-text "Message"]]

	  [:textarea.form-control
	   {:value       (:message @visitor_info)
	    :on-change   (on_visitor_info_change :message)
		:placeholder "Send me a question"
		:aria-label  "Leave a message"}]]
	 [:button.btn.btn-warning {:on-click on_post_anonymous_info_handle} "Send info"]
	 [render_error_message]
	 ]
	)


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
	   [render_anonymous_auth]
	   ]
	  ]
	 ]
	)
