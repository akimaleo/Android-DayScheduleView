# Description
This View destinies for manipulating events during the day;

# Usage
## XML view:
```<com.letit0or1.dayscheduleview.DayScheduleView
    android:id="@+id/dayScheduleView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:separatorHeight="1px"
    app:separatorPaddingLeft="20px"
    app:separatorPaddingRight="20px"
    android:isScrollContainer="true"
    android:layout_marginTop="1dp"/>
```

## Add events:
```
DayScheduleView view = (DayScheduleView) findViewById(R.id.dayScheduleView);

ArrayList<DrawableEvent> testE = new ArrayList<DrawableEvent>();

testE.add(new DrawableEvent(new Time(0, 10), new Time(1, 0)));
testE.add(new DrawableEvent(new Time(0, 20), new Time(2, 0)));
testE.add(new DrawableEvent(new Time(2, 30), new Time(4, 0)));
testE.add(new DrawableEvent(new Time(3, 30), new Time(7, 0)));
testE.add(new DrawableEvent(new Time(7, 10), new Time(8, 0)));

view.setEvents(testE);
```
Events will looks like:<br>
<img src="http://i65.tinypic.com/2198n41.jpg" width="256">
<img src="http://i67.tinypic.com/ok7mfp.jpg" width="256">
<img src="http://i63.tinypic.com/25rhx5g.jpg" width="256">

## Customising view
### XML attributes
You can customize view by changing attributes in XML layout.
 Available attributes at this time:

```
<attr name="separatorColor" format="color" />
<attr name="separatorColor" format="color" />
<attr name="separatorHeight" format="dimension" />
<attr name="separatorPaddingLeft" format="dimension" />
<attr name="separatorPaddingRight" format="dimension" />

<attr name="timeTextColor" format="color"/>
<attr name="timeTextSize" format="dimension" />

<attr name="hourFieldsHeight" format="dimension" />
<attr name="hourFieldsMinHeight" format="dimension" />
<attr name="hourFieldsMaxHeight" format="dimension" />
<attr name="hourMarginLeft" format="dimension" />

<attr name="eventTextColor" format="color" />
<attr name="eventTextSize" format="dimension" />
<attr name="eventBackgroundColor" format="color" />
```

Sample:<br>

```
<com.letit0or1.dayscheduleview.DayScheduleView
           android:id="@+id/dayScheduleView"
           android:layout_width="match_parent"
           app:separatorColor="#22d3ffc7"
           android:background="#22bebebe"
           app:eventTextColor="#000000"
           app:eventBackgroundColor="#7966ff00"
           android:layout_height="match_parent"/>
           ```

The result is:<br>
![XML edit result](https://pp.vk.me/c637830/v637830952/bc87/KO7Lz6zC7iM.jpg)

### Programming change
You can customize view by changing attributes in Java code:
```
//Define variable paint for separator
Paint dashPaint = new Paint();
dashPaint.setARGB(255, 255, 70, 200);
dashPaint.setStyle(Paint.Style.STROKE);
dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

//Define variable paint for rectangle
Paint paintRectangle = new Paint();
paintRectangle.setColor(Color.argb(100, 50, 60, 255));
//But, in Android Manifest.xml for blur effect, set
//android:hardwareAccelerated="false"
paintRectangle.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

//Apply changes for view
view.setPaintSeparator(dashPaint);
view.setPaintRectangle(paintRectangle);
```
The result is:<br>
<img src="https://pp.vk.me/c637618/v637618952/d9c2/h2JmoixvVEw.jpg" width="256">
