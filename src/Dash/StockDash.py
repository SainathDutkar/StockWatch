import dash
import dash_core_components as dcc
import dash_html_components as html
from mysql.connector import Error,MySQLConnection
from dbConfig import read_db_config
import plotly.graph_objs as go
import pandas as pd
from dash.dependencies import Input, Output, State
import datetime as dt



df = pd.DataFrame(columns = ['Time','Date','TimeStamp','editLink'])
df1 = pd.DataFrame(columns = ['User','Count']) 


app = dash.Dash()



app.layout=html.Div([
    html.Div([
        html.Div([
            html.H2('Enter the Name of the Company'),
			dcc.Input(id='input-1-state', type='text',value ='Microsoft'),
			html.Button(id='submit-button', n_clicks=0, children='Get Edits Statistics'),
            dcc.Graph(id='graph'),
			html.Div(id="hover-image")
        ], className="six columns", style={"height" : "50%", "width" : "100%"}),

        html.Div([
            html.Button(id='users-button', n_clicks=0, children='Get users Statistics'),
            dcc.Graph(id='Usergraph')
        ], className="six columns", style={"height" : "50%", "width" : "100%"}),
    ], className="row")
])

@app.callback(Output('graph', 'figure'),
              [Input('submit-button', 'n_clicks')],
              [State('input-1-state', 'value')])
def update_figure(n_clicks, input1):
    global conn
    db_config = read_db_config()
	
    try:
        conn = MySQLConnection(**db_config)
        if conn.is_connected():
            print('Connected to MySQL database')
      
        cursor = conn.cursor()
        
        query = "select * from History where name like '{}'".format(input1)
        cursor.execute(query)
        rows = cursor.fetchall()
        print(rows)
    
        i=0
        for row in rows:
            tempDateTime = row[5]
            df.loc[i] = [tempDateTime[:tempDateTime.find(",")],row[5][6:],row[5],row[6]]
            i = i+1
         
            
        print(df)
        df.set_index('TimeStamp')
    except Error as e:
        print(e)
 
 
    finally:
       cursor.close()
       conn.close()
       print('Connection closed.')
   

    traces = []

    
    preX = df["Date"].tolist()
    preY = df["Time"].tolist()
    
    newX = [dt.datetime.strptime(v.strip(),"%d %B %Y") for v in preX]
    newY = [dt.datetime.strptime(v.strip(),"%H:%M") for v in preY]


  
    traces.append(go.Scatter(
            x=newX,
            y=newY,
            mode='markers',
            opacity=0.7,
            marker={'size': 15}
        ))

    return {
        'data': traces,
        'layout': go.Layout(
                title = 'Page Edits on Wikipedia Page',
                xaxis = {'title': 'DAY'},
                yaxis = {'title': 'TIME'},
                hovermode='closest'

        )
    }
    

@app.callback(Output('Usergraph', 'figure'),
              [Input('users-button', 'n_clicks')],
              [State('input-1-state', 'value')])
def update_figure_user(n_clicks, input1):
    global conn
    db_config = read_db_config()
	
    try:
        conn = MySQLConnection(**db_config)
        if conn.is_connected():
            print('Connected to MySQL database')
      
        cursor1 = conn.cursor()
        
        cursor1.execute("select user,count(user) from History where name like 'Microsoft' group by (user)")
#        cursor.execute(query)
        rows = cursor1.fetchall()
        print(rows)
    
        i=0
        for row in rows:
           df1.loc[i] = [row[0],row[1]]
           i = i+1
     
    except Error as e:
        print(e)
 
 
    finally:
       cursor1.close()
       conn.close()
       print('Connection closed.')

    traces = []
    X =df1["User"].tolist()
    Y=df1["Count"].tolist()
     
    
    traces.append(go.Scatter(

            x=X,
            y=Y,
            mode='lines',
            opacity=0.7,

        ))

    return {
        'data': traces,
        'layout': go.Layout(
                title = 'Page Edits on Wikipedia Page',
                xaxis = {'title': 'USER NAME'},
                yaxis = {'title': 'Number of Edits'},
                hovermode='closest'
        )
    }    
 
    
@app.callback(
    Output('hover-image', 'children'),
    [Input('graph', 'clickData')])
def callback_image(hoverData):
    time=hoverData['points'][0]['y']
    date=hoverData['points'][0]['x']
    df['TimeStamp'] = df['TimeStamp'].astype(str)
    timeVal = time+','+date
    link = df.loc[df['TimeStamp']==timeVal,['editLink']]
    prevLink = link['editLink'].head(1)
    print(prevLink)
    return html.Iframe(src=prevLink,width="100%",height="100%")    
  
    

if __name__ == '__main__':
    app.run_server(debug=True)