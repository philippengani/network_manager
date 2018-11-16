package FindBts;

class Calculations {

    static int fetchPortNumber(double ca){
        int b=0;
        if((ca % 100)==0){
            int a=(int)(ca/100);
            if(a>5){
                b=5;
            }else {
                b=a;
            }
        }
        else {
            int a=(int)(ca/100);
            if(a!=5){
                b=a+1;
            }
            else {
                b=a;
            }
        }
        return b;
    }
    static String gigaIntPort(double ca){
        String intPo="";

        double a=ca/1000;
        int p1=(int)a;
        double t=a%1;
        double r =Math.round(t*100)/100D;
        //System.out.println(r);

        if ((r*1000)>500){
            int p=(p1+1);
            intPo= p+" ports of Gigabit only";
        }
        else {
            intPo= p1 +" Gigabit port and "+Calculations.fetchPortNumber(r*1000)+"Fast-Ethernet";
        }
        return intPo;
    }

}
