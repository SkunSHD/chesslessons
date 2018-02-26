(ns chesslessons.visitor-model
	(:require
		[chesslessons.firebase.db :as db]
        [chesslessons.firebase :refer [sign_out]]

        ;		Utils
        [chesslessons.normalize-visitor.utils :refer [normalize_visitor]]
		[chesslessons.atom.utils  :refer [atom! action!]]
))

(def log (.-log js/console))


; ==================
; Atoms
(defonce visitor (atom! "[visitor.model/visitor]" nil))


; ==================
; Actions
(defn set_visitor [new_visitor]
    (action! "[visitor.model/set_visitor]" new_visitor)
	(reset! visitor (normalize_visitor new_visitor))
    (db/save_visitor  (normalize_visitor new_visitor)))


(defn logout []
	(action! "[visitor.model/logout] of" (:name @visitor) @visitor)
    (sign_out))