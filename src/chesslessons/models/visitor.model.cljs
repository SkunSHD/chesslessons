(ns chesslessons.visitor-model
	(:require
		[chesslessons.firebase.db :as db]
;		Utils
		[chesslessons.normalize-user.utils :refer [normalize_user]]
		[chesslessons.atom.utils  :refer [atom! action!]]
))

(def log (.-log js/console))


; ==================
; Atoms
(defonce visitor (atom! "[visitor.model/user]" nil))


; ==================
; Actions
(defn set_user [new_visitor]
	(action! "[visitor.model/user]" new_visitor)
	(reset! user (normalize_user new_visitor))
	(db/save_visitor  (normalize_user new_visitor)))