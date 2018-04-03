(ns chesslessons.components.sign_in_anonymous.component
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


; ==================
; Privat
(defn- -sign_in_anonimously []
	(fbs/sign_in_anonymous))


(defn- -save_anonimous_info []
	(db/save_anonymous_message (js/parseInt (:phone @visitor_info)) (:message @visitor_info))
	)


; Form Validation
(defn- -required? [field_name]
	(let [required_fields '(:phone)]
		(some #(= field_name %) required_fields).
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
		:message (cond
					 (< (count field_value) 1000) {:valid? true}
					 :else {:valid? false :error_message "The message is too long (1000 characters is max)"}
					 )))


; ==================
; Handlers
(defn- -on_visitor_info_change [field_name]
	(fn [event]
		(let [new_field_value (.-value (.-currentTarget event))]
			;clear phone message
			(when
				(and
				 (-error_exist? field_name)
				 (not (empty? new_field_value)))
				(-clear_field_error_message field_name))

			(let [{ valid? :valid? error_message :error_message } (-validate field_name new_field_value)]
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


; ==================
; Render
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


(defn render_anonymous_auth [toggle_name]
	[:div {:style {:display (if (= toggle_name "anonymous" ) "block" "none")}}
	 [:div.input-group
	  [:div.input-group-prepend
	   [:span.input-group-text {:style {:padding-left 48}} "+38"]]
	  [:input.form-control {:value (:phone @visitor_info)
							:on-change (-on_visitor_info_change :phone)
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
		:on-change   (-on_visitor_info_change :message)
		:placeholder "Send me a question"
		:aria-label  "Leave a message"}]]
	 [:button.btn.btn-warning {:on-click on_post_anonymous_info_handle} "Send info"]
	 [render_error_message]
	 ]
	)