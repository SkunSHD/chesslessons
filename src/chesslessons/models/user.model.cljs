(ns chesslessons.user-model
	(:require
		[chesslessons.firebase.db :as db]
;		Utils
		[chesslessons.normalize-user.utils :refer [normalize_user]]
		[chesslessons.atom.utils  :refer [atom! action!]]
))

(def log (.-log js/console))


; ==================
; Atoms
(defonce user (atom! "[user.model/user]" nil))


; ==================
; Actions
(defn set_user [new_user]
	(action! "[user.model/user]" new_user)
	(reset! user (normalize_user new_user))
	(db/save_visitor  (normalize_user new_user)))