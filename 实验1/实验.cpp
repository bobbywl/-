/*
* Author：王龙 
* Instruction：1.无向图的创建、存储和搜索 ；2.txt文件的读取 
*/ 
#include <iostream>
#include <string>
#include <fstream>
#include <cassert>
#include <stdlib.h>
using namespace::std;
 
#define MaxSize 1200

struct Person{
	int id;
	struct Person *friends;
}; 
struct Person Map[MaxSize];
struct Person *pPerson,*node;

//结构体指针数组初始化
void init(){
	for(int i=0;i<MaxSize;i++){
		Map[i].id=i;
		Map[i].friends=NULL;
	}
}
//根据文件中的数据构建邻接表  
void creatMap(string file){
	ifstream infile; 
    infile.open(file.data());   //将文件流对象与文件连接起来 
    assert(infile.is_open());   //若失败,则输出错误消息,并终止程序运行 
    
    string lines,str_star,str_over;
    int index,star,over;
    while(getline(infile,lines)){
		index=lines.find(',');
		str_star.assign(lines,0,index);
    	str_over.assign(lines,index+1,lines.size());
		star = atoi(str_star.c_str());
		over = atoi(str_over.c_str());
		//采用单链表的前插值方案
		//star->over 
		pPerson = Map+star;
		node = new Person;
		node->id=over;
		node->friends=pPerson->friends;
		pPerson->friends=node;	
		//over->star
		pPerson = Map+over;
		node = new Person;
		node->id=star;
		node->friends=pPerson->friends;
		pPerson->friends=node;	
	}
	//关闭文件输入流 
    infile.close();             
}

//社交网路查询 搜索结构体指针素组+单链表遍历 
void searchRelative(){
	//依据输入查询关系
	int index;
	cout<<"\b请输入要查询的节点编号：";
	cin>>index;
	pPerson = Map+index;
	int numoffriends = 0;
	while(pPerson!=NULL){
		cout<<pPerson->id<<"->"; 
		numoffriends++;
		pPerson=pPerson->friends;
	}
	cout<<"  共有朋友: "<<numoffriends<<" 个";
	//依据输入判断两人是否为朋友关系 
	int oneP,twoP;
	cout<<endl<<"\b请输入要判断关系的两人";
	cin>>oneP>>twoP;
	pPerson = Map+oneP;
	while(pPerson!=NULL){
		if(pPerson->id==twoP){
			cout<<"\b两人是朋友!"<<endl;
			cout<<"-----------------------------------------------------"<<endl;
			break;
		}
		pPerson=pPerson->friends;
	}
	if(pPerson==NULL){
		cout<<"\b两人不是朋友!"<<endl;
		cout<<"-----------------------------------------------------"<<endl;
	}
}
/*程序入口*/
int main(){
	init();
	cout<<"\b初始化完成o(*￣▽￣*)ブ，即将构建社交网络，请稍等···"<<endl; 
	creatMap("C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt");
	cout<<"\b社交网络构建完成o(*￣▽￣*)ブ，请按Enter继续"<<endl; 
	cout<<"-----------------------------------------------------";
	while(getchar()!=EOF){
		searchRelative();
		system("pause");
		system("cls");
		cout<<"\b开始新的查询，或按Ctrl+C退出程序"<<endl<<endl;; 
	}

	return 0;
}

