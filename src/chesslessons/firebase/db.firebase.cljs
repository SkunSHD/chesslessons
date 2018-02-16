(ns chesslessons.firebase.db
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fb]))

(def log (.-log js/console))


(def firestore (.firestore js/firebase))

(def collections {
	 :users (.collection firestore "users")
})


; ==============
; Private
(defn- -users_exists? [users]
	(not (empty?(filter
	             (fn [user] (aget user "exists"))
	             (js->clj (aget users "docs"))))))


(defn get_user_by_uid [uid]
	(.get (.doc (:users collections) uid)))

(defn get_user_by_email [email]
	(.get (.where (:users collections) "email" "==" email)))

(defn get_user_all []
	(.get (:users collections)))

(defn save_user [new_user]
	(.then (get_user_by_email(:email new_user)) (fn [users]
		(if (-users_exists? users)
		  (log "User already exists: " (:email new_user))
			(.set (.doc (:users collections) (:uid new_user)) (clj->js new_user)) )
		))
	)

(defn delete_user [uid]
	(.then (.delete (.doc (:users collections) uid))
		#(log "delete user success, uid:" uid)
		#(log "delete user error" %))
	)

(defn add_listener_on_users_collection [callback]
	(.onSnapshot (:users collections) callback))