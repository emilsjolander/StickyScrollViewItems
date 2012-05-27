StickyScrollViewItems is made to be very simple to use, just replace you're current ScrollView with StickyScrollView and start tagging the views you want sticking to the top.

**USAGE**

Tag any view inside the StickyScrollView (as with a regular ScrollView only one child is aloud, i recommend using a LinearLayout, it is inside this layout that the sticky views should be marked) with either "sticky" or "sticky-nonconstant" to make it stick the the top of the scroll container until a new sticky tagged view comes along and pushes it away. Tag views that have a constant drawing (TextViews, ImageViews etc) with "sticky" and views where the drawing is not constant (Buttons with states, any view with animation etc) with "sticky-nonconstant".



**LICENSE**

Copyright 2012 Emil Sjölander

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.