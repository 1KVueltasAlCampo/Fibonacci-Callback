module Demo
{
    interface Callback{
	    void response(string rs );
        string waitForResult();
    }
    
    interface Printer
    {
        string printString(string s,string hostname,Callback* cl);
    }
}