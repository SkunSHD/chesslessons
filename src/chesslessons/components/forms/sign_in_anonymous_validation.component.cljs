(ns chesslessons.components.sign_in_anonymous_validation.component
	)

(def log (.-log js/console))


(defn- -required? [field_name]
	(let [required_fields '(:phone)]
		(some #(= field_name %) required_fields).
		)
	)


(defn- -is_tel_wrote? [visitor_info]
	(not (empty? (:phone @visitor_info)))
	)


(defn- -set_visitor_info_error [visitor_info field_name & [message]]
	(let [error_message (cond
							(= message "") ""
							(> (count message) 0) message
							(nil? message) (case field_name
											   :phone (if (empty? (field_name @visitor_info))
														  "Phone field is required"
														  "Type just numbers please")
											   :message "The message is too long (1000 characters is max)"))]
		(swap! visitor_info assoc-in [:error field_name] error_message)))


(defn- -clear_field_error_message[visitor_info field_name]
	(-set_visitor_info_error visitor_info field_name "")
	)


(defn- -error_exist? [visitor_info field_name]
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