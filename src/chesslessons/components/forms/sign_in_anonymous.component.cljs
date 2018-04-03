(ns chesslessons.components.sign_in_anonymous.component
	(:require
		[chesslessons.firebase :as fbs]
		[chesslessons.firebase.db :as db]
		[reagent.core :refer [atom]]
;       Models
		[chesslessons.visitor-model :as visitor_model]
; 		Components
		[chesslessons.components.sign_in_anonymous_validation.component :as validate]

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


(defn- -clean_form_fields []
	(reset! visitor_info {:phone "" :message "" :error {}}))


; ==================
; Handlers
(defn- -on_visitor_info_change [field_name]
	(fn [event]
		(let [new_field_value (.-value (.-currentTarget event))]
			;clear phone message
			(when
				(and
				 (validate/-error_exist? visitor_info field_name)
				 (not (empty? new_field_value)))
				(validate/-clear_field_error_message visitor_info field_name))

			(let [{ valid? :valid? error_message :error_message } (validate/-validate field_name new_field_value)]
				(if valid?
					(swap! visitor_info assoc field_name new_field_value)
					(validate/-set_visitor_info_error visitor_info field_name error_message)
					)
				)
			)
		)
	)


(defn- on_post_anonymous_info_handle [e]
	(.preventDefault e)
	(if (validate/-is_tel_wrote? visitor_info)
		(.then (-sign_in_anonimously) (fn []
										  (.then (-save_anonimous_info) (fn []
																			(.then (fbs/delete_current_visitor_from_firebase) (fn []
																																  (-clean_form_fields))))))
			   )
		(validate/-set_visitor_info_error visitor_info :phone "Phone field is required")
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
							:style {:border-color (when (validate/-error_exist? visitor_info :phone) "red")}
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