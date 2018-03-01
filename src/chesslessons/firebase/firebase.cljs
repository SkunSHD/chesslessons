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
; Facebook
(def facebook_auth_provider (new (.-FacebookAuthProvider (.-auth firebase))))


(defn facebook_link_user [visitor credential]
	(.then (.link visitor credential) (fn [wtf] (log "VSE" wtf))))


(defn facebook_fect_provider_for_email [email credential]
	(.then (.fetchProvidersForEmail (auth) email)
	       (fn [providers]
		       (.then (.signInWithEmailAndPassword (auth) email "ward121314")
		              (fn [visitor] (facebook_link_user visitor credential))))))


(defn facebook_auth_error [error]
	(log "response" error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
		(log "[CURE]: delete [visitor] in [firebase/authentification/visitors] with same email")))


; ==================
; Google
(def google_auth_provider (new (.-GoogleAuthProvider (.-auth firebase))))


(defn google_auth_error [error]
	(log error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
        (log "google_auth_error" error)))


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
				(.signInWithPopup (auth) facebook_auth_provider) callback)
			facebook_auth_error))


(defn google_auth [callback]
	(.catch (.then
				(.signInWithPopup (auth) google_auth_provider) callback)
			google_auth_error))