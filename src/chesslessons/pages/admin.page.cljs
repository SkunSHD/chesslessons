(ns chesslessons.admin.page
	(:require
;		History
		[chesslessons.history :as history :refer [nav!]]
;		Models
		[chesslessons.admin-model :as admin_model]
		[chesslessons.visitors-model :as visitors_model]
;		Components
		[chesslessons.components.admin.components :as admin_components]
))

(def log (.-log js/console))


; ==================
; Watchers
(add-watch admin_model/admin "ADMIN-MODEL-CHANGE-ADMIN" #(visitors_model/get_visitors))

; ==================
; Public
(defn render []
	[:div
	 [:button {:onClick #(nav! "/")} "go to home page"]
	    (if (nil? @admin_model/admin)
		  [admin_components/render_login_form]
		  [admin_components/render_admin_container])
	 ])