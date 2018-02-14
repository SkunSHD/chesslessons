(ns chesslessons.user-model
	(:require
		[chesslessons.firebase.db :as db]
;		Utils
		[chesslessons.normalize-user.utils :refer [normalize_user]]
		[chesslessons.atom.utils  :refer [atom!]]
))

(def log (.-log js/console))


; ==================
; Atoms
(defonce user (atom! "[user.model/user]" nil))


; ==================
; Public
(defn set_user [new_user]
	(log new_user "NEW!")
	(reset! user (normalize_user new_user))
	(db/save_user  (normalize_user new_user)))