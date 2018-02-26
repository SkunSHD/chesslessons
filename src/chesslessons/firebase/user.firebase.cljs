(ns chesslessons.firebase.user)

(def log (.-log js/console))

(defonce user (.-currentUser (.auth js/firebase)))


(defn upodate_user [fields]
	(.updateProfile (.-currentUser (.auth js/firebase)) fields))