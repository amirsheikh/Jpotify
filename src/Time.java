import java.io.Serializable;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

public class Time implements Serializable {
    private int second;
    private int minute;
    private int hour;

    public String getTime(){
        if(hour != 0) {
            if(minute>=10 && second>=10)
                return hour + ":" + minute + ":" + second;
            else if(minute>=10)
                return hour + ":" + minute + ":0" + second;
            else if(second>=10)
                return hour + ":0" + minute + ":" + second;
            else
                return hour + ":0" + minute + ":0" + second;
        }
        else {
            if(minute>=10 && second>=10)
                return minute + ":" + second;
            else if(minute>=10)
                return minute + ":0" + second;
            else if(second>=10)
                return "0" + minute + ":" + second;
            else
                return "0" + minute + ":0" + second;
        }
    }
    public void setTime(int postion){
        int count = (int) round(ceil(postion/38.4));
        second = count%60;
        minute = ((count - second)/60)%60;
        hour = (((count - second)/60 - minute)/60)%60;
    }
}
