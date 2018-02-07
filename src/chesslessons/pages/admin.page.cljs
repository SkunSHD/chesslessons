(ns chesslessons.admin.page
	(:require
;		History
		[chesslessons.history :as history :refer [nav!]]
;		Models
		[chesslessons.admin-model :as admin_model]
;		Components
		[chesslessons.components.admin.components :as admin_components]
))


(defn render []
	[:div
	 [:button {:onClick #(nav! "/")} "go to home page"]
	    (if (nil? @admin_model/admin)
		  [admin_components/render_login_form]
		  [admin_components/render_admin_container])
	 ])