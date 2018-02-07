(ns chesslessons.layout
	(:require
	[chesslessons.history :refer [current_page]]
;	Pages
	[chesslessons.home.page :as home_page]
	[chesslessons.admin.page :as admin_page]
	[chesslessons.page404.page :as page404_page]
))


; ==================
; Private
(defn render_current_page []
	(case  (:name @current_page)
		"home_page" [home_page/render]
		"admin_page" [admin_page/render]
		[page404_page/render]))


; ==================
; Public
(defn render []
	[render_current_page])