(ns chesslessons.firebase.user.firebase)

(def log (.-log js/console))

(defonce user (.-currentUser (.auth js/firebase)))


(defn upodate_user [fields]
	(log user)
	(.catch (.then (.updateProfile user fields)
	        (fn [user] (log user "SUC")))
	        (fn [e] (log e "ERR"))))