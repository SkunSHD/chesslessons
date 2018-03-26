(ns chesslessons.search-model
	(:require
		[reagent.core :refer [atom cursor]]
; 		Models
		[chesslessons.visitors-model :refer [visitors deleted_visitors anonymous_visitors]]
; 		Utils
		[clojure.string :as s]
		))


(def log (.-log js/console))


; ==================
; Atoms
(defonce search (atom ""))


; ==================
; Privat
(defn- -match_visitor [visitor query]
	; matching search_fields in visitor
	(not (empty? (filter (fn [keyval]
							 (let [key (first keyval)
								   val (second keyval)
								   search_fields '(:name :email :phone :message)]
								 (when
									 (and
										 (some #(= % key) search_fields)
										 val)
									 (cond
										 (string? val) (s/includes? (s/lower-case val) query)
										 (number? val) (s/includes? (str val) query)))
										 )) visitor)))
	)


(defn- -get_visitors_from_collection [collection_name]
	(case collection_name
				:visitors (flatten @visitors)
				:deleted_visitors (flatten @deleted_visitors)
				:anonymous_visitors (flatten @anonymous_visitors)))


; ==================
; Public
(defn set_search [query]
	(reset! search query))


(defn on_search_change [event]
	(set_search (.-value(.-target event)))
	)


(defn is_searching []
	(not (empty? @search)))


(defn search_visitors [collection_name]
	(if (is_searching)
		(let [query (s/lower-case @search) filtered_visitors (filter #(-match_visitor % query) (-get_visitors_from_collection collection_name))]
			filtered_visitors
			)))
