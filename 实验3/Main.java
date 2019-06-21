package WangLong.Project3;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * 读取txt数据，并利用HashMap和HashSet存储构建社交网络 利用HashMap和Vector存储
 * 
 * @author Bobby
 * @data 2019年4月8日 下午4:18:07
 */

public class Main {
	public static List<Integer[]> txtList = null;// 读取txt数据获得的LIst对象
	public static HashMap<Integer, HashSet<Integer>> networks = null;// 社交网络的HashMap对象
	public static HashMap<Integer, Vector<Integer>> DegreeMap = null;// 网络中节点度信息的HashMap对象

	/**
	 * 程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 数据读取
		String dataPath = "C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt";// 数据文件路径
		readFile(dataPath);
		// 哈希表构建
		creatHashMap();
		// 节点度的统计
		creatDegreeMap();
		// 网络查询
		Scanner in = new Scanner(System.in);
		Integer degree = null;
		// 使用折线图显示
		creatLineChart(DegreeMap);
		while (true) { 
			// 查找度
			System.out.print("输入查询的度数: ");
			try {
				degree = in.nextInt();
				getDegree(degree);
			}catch(Exception e) {
				System.out.println("没有节点的度为："+ degree+ "");
			}
			System.out.println();
		}
	}

	private static void creatLineChart(HashMap<Integer, Vector<Integer>> data) {
		// 图表字体设置
		StandardChartTheme mChartTheme = new StandardChartTheme("CN");
		mChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 15));
		mChartTheme.setExtraLargeFont(new Font("黑体", Font.PLAIN, 15));
		mChartTheme.setRegularFont(new Font("黑体", Font.PLAIN, 15));
		ChartFactory.setChartTheme(mChartTheme);

		// 图表数据集设置
		CategoryDataset mDataset = GetDataset(data);

		// 图表构造 显示属性设置
		JFreeChart mChart = ChartFactory.createLineChart("", // 图名字
				"节点的度", // 横坐标
				"数量", // 纵坐标
				mDataset, // 数据集
				PlotOrientation.VERTICAL, false, // 显示图例
				true, // 采用标准生成器
				false);// 是否生成超链接
		// 背景属性设置
		CategoryPlot mPlot = (CategoryPlot) mChart.getPlot();
		mPlot.setBackgroundPaint(Color.LIGHT_GRAY);// 设置底部背景色
		mPlot.setRangeGridlinePaint(Color.WHITE); // 设置水平方向背景线颜色
		mPlot.setRangeGridlinesVisible(true);// 设置是否显示水平方向背景线,默认值为true
		mPlot.setDomainGridlinePaint(Color.WHITE); // 设置垂直方向背景线颜色
		mPlot.setDomainGridlinesVisible(true); // 设置是否显示垂直方向背景线,默认值为false
		mPlot.setOutlinePaint(Color.WHITE);// 边界线

		// 设置折线属
		LineAndShapeRenderer lasp = (LineAndShapeRenderer) mPlot.getRenderer();// 获取显示线条的对象
		lasp.setBaseShapesVisible(true);// 设置拐点是否可见/是否显示拐点
		lasp.setDrawOutlines(true);// 设置拐点不同用不同的形状
		lasp.setUseFillPaint(true);// 设置线条是否被显示填充颜色
		lasp.setBaseFillPaint(Color.GREEN);// 设置拐点颜色
		lasp.setSeriesShape(0, new Rectangle2D.Double(-5.0D, -5.0D, 5.0D, 5.0D));// 设置形状
		lasp.setDrawOutlines(false);

		// 图表显示
		ChartFrame mChartFrame = new ChartFrame("累计概率分布", mChart);
		mChartFrame.pack();
		mChartFrame.setVisible(true);
	}

	/**
	 * 	计算累计度分布概率，并为绘制图表添加数据
	 * @param data
	 * @return
	 */
	public static CategoryDataset GetDataset(HashMap<Integer, Vector<Integer>> data) {
		DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
		/*** 累计统计 ***/
		int total_degree;
		for(Integer index : data.keySet()) {
			total_degree = 0;
			for (Integer degree : data.keySet()) {
				if(data.get(degree).size() >= data.get(index).size())  total_degree = total_degree + data.get(degree).size();
			}
			mDataset.addValue(total_degree/data.keySet().size(), "First", index.toString());
		}
		return mDataset;
	}

	/**
	 * 获取度为degree的所有人员的ID
	 * 
	 * @param degree 待查询的度
	 */
	private static void getDegree(Integer degree) {
		System.out
				.println("--------- 朋友数为 " + degree + " 的人共有：" + DegreeMap.get(degree).size() + "  人- - - - - - - - -");
		System.out.print("名单：");
		for (Integer index : DegreeMap.get(degree)) {
			System.out.print(index + " ");
		}
		System.out.println();
		System.out.println("------------------------------------");
	}

	/**
	 * 创建以节点数为Key，成员id为Value的HashMap
	 */
	private static void creatDegreeMap() {
		if (DegreeMap == null)
			DegreeMap = new HashMap<Integer, Vector<Integer>>();
		for (Integer key : networks.keySet()) {
			if (!DegreeMap.containsKey(networks.get(key).size()))
				DegreeMap.put(networks.get(key).size(), new Vector<Integer>());
			DegreeMap.get(networks.get(key).size()).add(key);
		}
	}

	/**
	 * 创建社交网网络
	 */
	private static void creatHashMap() {
		if (networks == null)
			networks = new HashMap<Integer, HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {
			lineInteger = it.next();
			/* System.out.println(lineInteger[0]+" "+lineInteger[1]); */
			// 将两人的关系添加到网络中
			addRealtionship(lineInteger[0], lineInteger[1]);
			addRealtionship(lineInteger[1], lineInteger[0]);
		}
	}

	/**
	 * 在networks为成员peo1添加朋友peo2
	 * 
	 * @param peop1 人员1id
	 * @param peop2 人员2id
	 */
	private static void addRealtionship(Integer peop1, Integer peop2) {
		if (!networks.containsKey(peop1)) {
			networks.put(peop1, new HashSet<Integer>());
		}
		networks.get(peop1).add(peop2);
	}

	/**
	 * 读入txt文件，并将数据文件中的数据保存在成员变量txtList中
	 * 
	 * @param dataPath 数据文件路径
	 */
	public static void readFile(String dataPath) {
		if (txtList == null)
			txtList = new ArrayList<Integer[]>();
		File txt = new File(dataPath);
		Reader reader = null;
		BufferedReader buf = null;
		try {
			reader = new FileReader(txt);
			buf = new BufferedReader(reader);
			String str = null;
			while ((str = buf.readLine()) != null) {
				/* System.out.println(str); */
				txtList.add(new Integer[] { Integer.parseInt(str.split(",")[0]), Integer.parseInt(str.split(",")[1]) });
			}
			buf.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件读取错误！");
			e.printStackTrace();
		}
	}

}
