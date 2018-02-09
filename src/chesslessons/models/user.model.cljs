(ns chesslessons.user-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase.db :as db]
;		Utils
		[chesslessons.normalize-user.utils :refer [normalize_user]]
))

(def log (.-log js/console))


; ==================
; Atoms
(defonce user (atom nil))



; ==================
; Public
(defn set_user [new_user]
	(reset! user (normalize_user new_user))
	(db/save_user  (normalize_user new_user)))