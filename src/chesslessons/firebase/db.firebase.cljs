(ns chesslessons.firebase.db
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fb]))

(def log (.-log js/console))


(def firestore (.firestore js/firebase))

(def collections {
	 :visitors (.collection firestore "visitors")
})


; ==============
; Private
(defn- -visitors_exists? [visitors]
	(not (empty?(filter
	             (fn [visitor] (aget visitor "exists"))
	             (js->clj (aget visitors "docs"))))))


(defn get_visitor_by_email [email]
	(.get (.where (:visitors collections) "email" "==" email)))

(defn get_all_visitors []
	(.get (:visitors collections)))

(defn save_visitor [new_visitor]
	(.then (get_visitor_by_email (:email new_visitor)) (fn [visitors]
		(if (-visitors_exists? visitors)
		  (log "Visitor already exists: " (:email new_visitor))
			(.set (.doc (:visitors collections) (:uid new_visitor)) (clj->js(merge new_visitor { :timestamp (.-now js/Date) }))) )
		))
	)

(defn delete_visitor [uid]
	(.then (.delete (.doc (:visitors collections) uid))
		#(log "delete visitor success, uid:" uid)
		#(log "delete visitor error" %))
	)

(defn add_listener_on_visitors_collection [callback]
	(.onSnapshot (:visitors collections) callback))