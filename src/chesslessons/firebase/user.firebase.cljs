(ns chesslessons.firebase.user.firebase)

(defonce user (.-currentUser (.auth (js/firebase))))


(defn upodate_user [fields]
	(.updateProfile user fields))