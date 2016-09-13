# Description
This View destinies for manipulating events during the day;

# Usage
### XML view:
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

### Add events:
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
