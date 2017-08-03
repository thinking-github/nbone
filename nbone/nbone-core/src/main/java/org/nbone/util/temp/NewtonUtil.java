package org.nbone.util.temp;

/**
 * 牛顿插值法：用于数据补缺
 *
 */
public class NewtonUtil {
	
	/**
	 * 牛顿插值法
	 * @param px	X轴坐标列表
	 * @param py	Y轴坐标列表
	 * @param x		需要插值的x值
	 * @return		x值对应的y值
	 */
	public final static double newton(double[] px, double[] py, double x){
		int n = px.length-1;
		
		double[] g = new double[n+1];
		
		//计算差商表个g[k],k=0,1,,n
	    for (int i=0; i<n+1; i++){
	        g[i] = py[i];
	    }
	    for (int k=1; k<n+1; k++){
	        for (int i=n; i>=k; i--){
	        	//g[i]用来暂时存放f[X(i-k), X(i)]
	            g[i] = (g[i]-g[i-1])/(px[i]-px[i-k]);
	        }
	    }
	    double t = 1;//基函数
	    double newton = py[0];
	    for (int i=1; i<n; i++){
	    	//基函数递推式
	        t = (x - px[i-1]) * t;
	        newton = newton +  t * g[i];
	    }
	    return newton;
	}
}
