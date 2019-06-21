/*
* Author������ 
* Instruction��1.����ͼ�Ĵ������洢������ ��2.txt�ļ��Ķ�ȡ 
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

//�ṹ��ָ�������ʼ��
void init(){
	for(int i=0;i<MaxSize;i++){
		Map[i].id=i;
		Map[i].friends=NULL;
	}
}
//�����ļ��е����ݹ����ڽӱ�  
void creatMap(string file){
	ifstream infile; 
    infile.open(file.data());   //���ļ����������ļ��������� 
    assert(infile.is_open());   //��ʧ��,�����������Ϣ,����ֹ�������� 
    
    string lines,str_star,str_over;
    int index,star,over;
    while(getline(infile,lines)){
		index=lines.find(',');
		str_star.assign(lines,0,index);
    	str_over.assign(lines,index+1,lines.size());
		star = atoi(str_star.c_str());
		over = atoi(str_over.c_str());
		//���õ������ǰ��ֵ����
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
	//�ر��ļ������� 
    infile.close();             
}

//�罻��·��ѯ �����ṹ��ָ������+��������� 
void searchRelative(){
	//���������ѯ��ϵ
	int index;
	cout<<"\b������Ҫ��ѯ�Ľڵ��ţ�";
	cin>>index;
	pPerson = Map+index;
	int numoffriends = 0;
	while(pPerson!=NULL){
		cout<<pPerson->id<<"->"; 
		numoffriends++;
		pPerson=pPerson->friends;
	}
	cout<<"  ��������: "<<numoffriends<<" ��";
	//���������ж������Ƿ�Ϊ���ѹ�ϵ 
	int oneP,twoP;
	cout<<endl<<"\b������Ҫ�жϹ�ϵ������";
	cin>>oneP>>twoP;
	pPerson = Map+oneP;
	while(pPerson!=NULL){
		if(pPerson->id==twoP){
			cout<<"\b����������!"<<endl;
			cout<<"-----------------------------------------------------"<<endl;
			break;
		}
		pPerson=pPerson->friends;
	}
	if(pPerson==NULL){
		cout<<"\b���˲�������!"<<endl;
		cout<<"-----------------------------------------------------"<<endl;
	}
}
/*�������*/
int main(){
	init();
	cout<<"\b��ʼ�����o(*������*)�֣����������罻���磬���Եȡ�����"<<endl; 
	creatMap("C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt");
	cout<<"\b�罻���繹�����o(*������*)�֣��밴Enter����"<<endl; 
	cout<<"-----------------------------------------------------";
	while(getchar()!=EOF){
		searchRelative();
		system("pause");
		system("cls");
		cout<<"\b��ʼ�µĲ�ѯ����Ctrl+C�˳�����"<<endl<<endl;; 
	}

	return 0;
}

