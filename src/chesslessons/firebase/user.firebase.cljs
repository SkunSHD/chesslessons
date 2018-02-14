(ns chesslessons.firebase.user.firebase)

(def log (.-log js/console))

(defonce user (.-currentUser (.auth js/firebase)))


(defn upodate_user [fields]
	(.updateProfile user fields))