(ns chesslessons.firebase.db-connect
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fb]
		;Models
		[chesslessons.admin-model :as admin_model]))

(def log (.-log js/console))


(def firestore (.firestore js/firebase))

(def collection {:users (.collection firestore "users")})


(defn write_user_data [user]
	(log "111 " user (:email user) (:uid user))

	(.add (:users collection) (clj->js{:user (:email user) :uid (:uid user)}))
	)


(defn save-current-user []
	(write_user_data @admin_model/admin)
	)