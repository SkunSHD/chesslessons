(ns chesslessons.visitors-model
  (:require
    [chesslessons.firebase.db :as db]
;		Utils
    [chesslessons.normalize-user.utils :refer [normalize_user normalize_visitors]]))


(def log (.-log js/console))


; ==================
; Atoms
(defonce visitors (atom nil))
(defonce visitors_error_msg (atom ""))


; ==================
; Public
(defn set_visitors [new_visitors]
  (log "all users" new_visitors)
  (reset! visitors new_visitors))

(defn set_visitors_error_msg [errors]
  (log "set_visitors error" errors)
  (reset! visitors errors))

(defn get_visitors []
  (.catch (.then (db/get_user_all)
                         (fn [all_docs] (set_visitors (normalize_visitors all_docs))))
                  (fn [error] set_visitors_error_msg (.-message error))))