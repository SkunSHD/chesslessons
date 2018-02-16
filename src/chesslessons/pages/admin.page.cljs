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
(defn- -on_change_admin [key atom old new]
	(if-not old (visitors_model/get_visitors))
	(visitors_model/add_listener_visitors_change)
	)

(add-watch admin_model/admin "[admin.page] ADMIN-MODEL-CHANGE-ADMIN" -on_change_admin)


; ==================
; Public
(defn render []
	[:div
	 [:button {:onClick #(nav! "/")} "go to home page"]
	    (if (nil? @admin_model/admin)
		  [admin_components/render_login_form]
		  [admin_components/render_admin_container])
	 ])