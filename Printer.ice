module Demo
{
    interface Callback{
	    void response(string rs );
    }
    
    interface Printer
    {
        string printString(string s,string hostname,Callback* cl);
    }
}