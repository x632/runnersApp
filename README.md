# runnersApp
This project stems from the YouTube course Running app by Philipp Lackner. Big thanks to him, especially for the foreground service and notification system which were lacking in my other run trackers. 

Additions and changes that I have made:

1. Added the possibility to delete runs, also added a small "delete/close" button to the run-views in the recyclerview for this, and of course a "sure"-dialog.
2. Added a green start marker and a red stop marker in the map-screenshot that is saved for every run.
3. In the tracking fragment I added live viewing of Distance ran in meters, live viewing of Average speed this far, and finally live viewing of current speed.
4. I have done quite a few changes to the formatting (and rounding) to most of the data that is displayed - this is mainly to suit my very own running needs. 
5. I have fixed some bugs in the statistics fragment and markerView - it now shows the correct markerView for the bar - and the latest run is to the right in the view. 
6. I have removed the horizontal landscape views and fixated the activity for portrait (mainly because I was too lazy to change all layouts).

Works fine for me at this point.




