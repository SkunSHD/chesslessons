(ns chesslessons.visitor-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase.db :as db]
;		Utils
        [chesslessons.normalize-visitor.utils :refer [normalize_visitor]]
		[chesslessons.atom.utils  :refer [atom! action!]]
))

(def log (.-log js/console))


; ==================
; Atoms
(defonce visitor (atom! "[visitor.model/visitor]" nil))
(defonce visitor_message (atom""))


; ==================
; Public
(defn set_visitor_message [text]
	(reset! visitor_message text))


; ==================
; Actions
(defn set_visitor [new_visitor]
    (action! "[visitor.model/set_visitor]" new_visitor)
	(reset! visitor (normalize_visitor new_visitor))
    (db/save_visitor  (normalize_visitor new_visitor) @visitor_message))