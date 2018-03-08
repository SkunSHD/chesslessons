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
	;	(if (or (s/includes?  (s/lower-case(:name visitor)) (s/lower-case @search))
	;			(s/includes?  (s/lower-case(:email visitor)) (s/lower-case @search)))
	;		(log "match " (:name visitor) (:email visitor))
	;		)
		(log "match " (:name visitor) (:email visitor))
		(log "-------------00-------------")
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
	(log "here" (is_searching) (s/lower-case @search))
	(if (is_searching)
		(let [filtered_visitors (filter #(-match_visitor % (s/lower-case @search)) (flatten @visitors))]
			filtered_visitors
			)))
