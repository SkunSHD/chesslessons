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
(defn- -user_exists? [users]
	(not (empty?(filter
	             (fn [user] (aget user "exists"))
	             (js->clj (aget users "docs"))))))



(defn get_user_by_uid [uid]
	(.get (.doc (:users collections) uid)))

(defn get_user_by_email [email]
	(.get (.where (:users collections) "email" "==" email)))


(defn save_user [new_user]
	(.then (get_user_by_email(:email new_user)) (fn [users]
		(if (-user_exists? users)
		  (log "User already exists: " (:email new_user))
		  (.add (:users collections) (clj->js new_user)) )
		))
	)