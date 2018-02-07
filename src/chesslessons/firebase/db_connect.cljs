(ns chesslessons.firebase.db-connect
	(:require
		[chesslessons.firebase :as fb]))

(def log (.-log js/console))

(def db-ref (.database js/firebase))

(defn save-current-user []
	(aget (fb/auth) "currentUser" "uid")
	(log db-ref))