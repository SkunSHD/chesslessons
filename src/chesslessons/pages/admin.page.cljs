(ns chesslessons.admin.page
	(:require
;		Models
		[chesslessons.admin-model :as admin_model]
;		Components
		[chesslessons.components.admin.components :as admin_components]
))


(defn render []
	[:div
	    (if (nil? @admin_model/admin)
		  [admin_components/render_login_form])
	 ])