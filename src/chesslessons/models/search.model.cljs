(ns chesslessons.search-model
	(:require
		[reagent.core :refer [atom cursor]]
; 		Models
		[chesslessons.visitors-model :refer [visitors]]
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
	(let [name (s/lower-case(:name visitor))
		  email (s/lower-case(:email visitor))]

		(or (s/includes? name  query)
			(s/includes? email query))
		)
	)


; ==================
; Public
(defn set_search [query]
	(reset! search query))


(defn on_search_change [event]
	(set_search (.-value(.-target event)))
	)


(defn is_searching []
	(not (empty? @search)))


(defn search_visitors []
	(if (is_searching)
		(let [query (s/lower-case @search) filtered_visitors (filter #(-match_visitor % query) (flatten @visitors))]
			filtered_visitors
			)))
