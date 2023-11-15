import React from 'react';
import ReactDOM from 'react-dom';
import {FrappeGantt, Task} from './frappe-gantt/index';

const startTask = [
    new Task({
        id: "INITIALIZING",
        name: "",
        start: undefined,
        end: undefined,
        url: "",
        progress: 100,
    },)
]

console.log('Dynamic created');

export default class DynamicTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            mode: 'Week',
            tasks: startTask,
        }
    }

    getIssuesPromise() {
        return new Promise(async resolve => {

            let fieldsResponse = await fetch(base_url + '/rest/api/2/field', {
                method: 'GET',
                headers: {
                    "Accept": "application/json",
                    'Content-Type': 'application/json'
                }
            })
            let fieldData = await fieldsResponse.json();
            let startField;
            let endField;
            let epicField;
            let teamField;

            for (let data of fieldData) {
                if (data.name === 'Start of Work') {
                    startField = data.id;
                }
                if (data.name === 'End of Work') {
                    endField = data.id;
                }
                if (data.name === 'Epic Link') {
                    epicField = data.id;
                }
                if (data.name === 'Associated Team') {
                    teamField = data.id;
                }
            }

            console.log(startField + endField + epicField)


            let newTaskArray = [];
            console.log(window.location.origin)
            let response = await fetch(base_url + '/rest/api/2/search?jql=type=request AND assignee= "' + userKey + '"',
                {
                method: 'GET',
                headers: {
                    "Accept": "application/json",
                    'Content-Type': 'application/json'
                }
            })

            let data = await response.json();
            for (let obj in Array.from(data.issues)) {
                let key = data.issues[obj].key
                let name = data.issues[obj].fields.summary
                let start = data.issues[obj].fields[startField]
                let end = data.issues[obj].fields[endField]
                let epic = data.issues[obj].fields[epicField]

//        console.log(key + name + start + end)
                newTaskArray.unshift(new Task({
                    id: key,
                    name: name,
                    start: start,
                    end: end,
                    epic: epic,
                    url: base_url + '/browse/' + key,
                    progress: 100,
                }))
            }
            console.log(newTaskArray);
            resolve(newTaskArray);
        })
    }

    async updateTasks() {
        let newTaskArray = await this.getIssuesPromise();
        console.log("working")
        if (newTaskArray === this.state.tasks) {
            return
        }

        console.log("newTaskArray:" + newTaskArray)
        console.log("state.tasks: ")
        console.log(this.state.tasks)

        this.setState({
            tasks: newTaskArray,
        })
    }

    componentDidMount() {
        this.updateTasks();
    }

    componentDidUpdate(prevProps, prevState) {
    }

    render() {
        console.log('Rendering')
        return (
            <div className="planner-container">
                <div onChange={(o) => {
                    this.setState({mode: o.target.value})
                }}>
                    <input type="radio" value="Day" name="viewMode" checked={this.state.mode === "Day"}/>Day
                    <input type="radio" value="Week" name="viewMode" checked={this.state.mode === "Week"}/>Week
                    <input type="radio" value="Month" name="viewMode" checked={this.state.mode === "Month"}/>Month
                    {/*<input type="radio" value="Year" name="viewMode" checked={this.state.mode === "Year"}/>Year*/}
                </div>

                <FrappeGantt
                    tasks={this.state.tasks}
                    viewMode={this.state.mode}
                    onClick={task => {
                        window.location = task.url
                    }}
                    custom_popup_html={
                        function (task) {
                            // the task object will contain the updated
                            // dates and progress value
                            return `<div class="details-container">
                            <a href="${task.url}"><div class="title">${task.name}</div></a>
                            <div class="subtitle">
                            <p>Expected to finish by ${task.end}</p>
		                    <p>${task.progress}% completed!</p>
		                    </div>		                
		                    </div>`;
                        }
                    }
                    onDateChange={(task, start, end) => console.log(task, start, end)}
                    onProgressChange={(task, progress) => console.log(task, progress)}
                    onTasksChange={tasks => console.log(tasks)}
                />
            </div>
        );
    }
}

window.addEventListener('load', function () {
    const wrapper = document.getElementById("container");
    wrapper ? ReactDOM.render(<DynamicTable/>, wrapper) : false;
});