(ns chesslessons.firebase
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase.user :refer [upodate_user]]
;		Utils
		[chesslessons.atom.utils  :refer [atom! action!]]
		))

(def log (.-log js/console))

(def firebase js/firebase)


(defn auth []
	(.auth firebase))


(defn sign_in_with_email_and_password [email password]
	(.signInWithEmailAndPassword (auth) email password))


(defn sign_out []
	(.signOut (auth)))


; ==================
; Atoms
(defonce unsubscribe_collection_functions
	(atom! "atom! [firebase/unsubscribe_collection_functions]" {:visitors nil :deleted_visitors nil}))


; ==================
; Privat
(defn delete_current_visitor_from_firebase []
	(let [current_visitor (.-currentUser (auth))]
		(.then (.delete current_visitor)
				   #(log "visitor deleted from firebase successfully" current_visitor)
				   #(log "visitor not deleted from firebase" %))
		)
	)


(defn -auth_error-handler [error]
	(log error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
		(log "auth_error" error)))


(def -facebook_auth_provider (new (.-FacebookAuthProvider (.-auth firebase))))


(def -google_auth_provider (new (.-GoogleAuthProvider (.-auth firebase))))


; ==================
; Public
(defn set_unsubscribe_collection_func [collection_name func]
	(swap! unsubscribe_collection_functions assoc collection_name func))


(defn unsubscribe_collections []
	; https://stackoverflow.com/questions/6685916/how-to-iterate-over-map-keys-and-values
	(doseq [[key val] @unsubscribe_collection_functions]
		(let [item_unsubscribe_func (key @unsubscribe_collection_functions)]
			(if-not (nil? item_unsubscribe_func) (do
													 (item_unsubscribe_func)
													 (swap! unsubscribe_collection_functions assoc key nil)))
			))
	)


(defn facebook_auth [callback]
	(.catch (.then
				(.signInWithPopup (auth) -facebook_auth_provider) callback)
			-auth_error-handler))


(defn google_auth [callback]
	(.catch
		(.then
			(.then (.signInWithPopup (auth) -google_auth_provider) callback
				   -auth_error-handler)
			delete_current_visitor_from_firebase)))