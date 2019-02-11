import React, {Component} from 'react';
import {connect} from 'react-redux'
import axios from 'axios';
import {
    ListGroup,
    Button,
    ButtonToolbar,
    Dropdown,
    Modal,
    ToggleButton,
    ToggleButtonGroup
} from 'react-bootstrap'

import Head from './part/head'

class Chef extends Component {
    constructor(props) {
        super(props);
        this.loadChefs = this.loadChefs.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleChangeDepartment = this.handleChangeDepartment.bind(this);
        this.handleChangeOperatingMode = this.handleChangeOperatingMode.bind(this);
        this.handleChangeWorkShift = this.handleChangeWorkShift.bind(this);
        this.sendChefs = this.sendChefs.bind(this);
        this.deleteChef = this.deleteChef.bind(this);
        this.getChefFromState = this.getChefFromState.bind(this);
        this.changeChef = this.changeChef.bind(this);
        this.addChef = this.addChef.bind(this);
        this.state = {
            id: "",
            show: false,
            firstName: "",
            middleName: "",
            lastName: "",
            durationWorkDay: "",
            departments: ["Russian"],
            operatingMode: "5/2",
            workShift: "Morning",
            update: false,
        };
    }

    componentWillMount() {
        this.loadChefs();
    }

    loadChefs() {
        axios.get('/chef/all')
            .then(res => {
                this.props.onGetChefs(res.data);
            })
    }

    handleClose() {
        this.setState({show: false});
    }

    handleShow() {
        this.setState({show: true});
    }

    handleChangeDepartment(departments, event) {
        this.setState({departments});
    }

    handleChangeOperatingMode(operatingMode, event) {
        this.setState({operatingMode})
    }

    handleChangeWorkShift(workShift, event) {
        this.setState({workShift})
    }

    getChefFromState() {
        const chef = {};
        chef.firstName = this.firstName.value;
        chef.middleName = this.middleName.value;
        chef.lastName = this.lastName.value;
        chef.durationWorkDay = this.durationWorkDay.value;
        chef.departments = [];
        for (let i = 0; i < this.state.departments.length; i++) {
            chef.departments.push(this.state.departments[i]);
        }
        chef.workShift = this.state.workShift;
        chef.operatingMode = {};
        const str = this.state.operatingMode.split("/");
        chef.operatingMode.countWorkingDay = str[0];
        chef.operatingMode.countDayOff = str[1];
        return chef;
    }

    sendChefs() {
        const chef = this.getChefFromState();
        let url = "/chef/add";
        if (this.state.update == true) {
            url = "/chef/update";
            chef.id = this.state.id;
        }
        axios.post(url, chef)
            .then(res => {
                if (typeof res.data.error === "undefined") {
                    // alert("Data was successfully loaded");
                    // const chefs = [];
                    // chefs.push(res.data.value);
                    // this.props.onAddChef(chef)
                    this.loadChefs();
                } else {
                    alert(res.data.error + " Repeat please")
                }
            })
            .catch((error) => {
                alert(error.message)
            })
            .finally(() => {
                this.handleClose();
            })
    }

    deleteChef(chef) {
        axios.post('/chef/delete', chef)
            .then(res => {
                if (typeof res.data.error === "undefined") {
                    alert("Data was successfully deleted");
                    this.loadChefs();
                } else {
                    alert(res.data.error + " Repeat please")
                }
            })
            .catch((error) => {
                alert(error.message)
            })
    }

    changeChef(chef) {
        const departments = [];
        for (let i = 0; i < chef.departments.length; i++) {
            departments.push(chef.departments[i].name);
        }
        this.setState({
            id: chef.id,
            firstName: chef.firstName,
            middleName: chef.middleName,
            lastName: chef.lastName,
            durationWorkDay: chef.durationWorkDay,
            operatingMode: chef.operatingMode.countWorkingDay + "/" + chef.operatingMode.countDayOff,
            workShift: chef.workShift,
            departments: departments,
            update: true,
            show: true
        });
    }

    addChef() {
        this.setState({
            firstName: "",
            middleName: "",
            lastName: "",
            durationWorkDay: "",
            operatingMode: "",
            workShift: "",
            departments: [],
            update: false
        });
        this.handleShow();
    }

    render() {
        return (
            <div>
                <Head/>
                <ListGroup>
                    {this.props.data.map((chef) =>
                        <ListGroup.Item key={chef.id}>
                            <ul className="list-chefs">
                                <li className="chef-name">{
                                    chef.lastName + " " +
                                    chef.firstName + " " +
                                    chef.middleName
                                }
                                </li>
                                <li>
                                    <label>Work shift:</label>
                                    {" " + chef.workShift}
                                </li>
                                <li>
                                    <label>Operating mode:</label>
                                    {" " + chef.operatingMode.countWorkingDay + "/" + chef.operatingMode.countDayOff}
                                </li>
                                <li>
                                    <label>Duration work day:</label>
                                    {" " + chef.durationWorkDay}
                                </li>
                                <li>
                                    <Dropdown>
                                        <Dropdown.Toggle variant="success">Qualification</Dropdown.Toggle>
                                        <Dropdown.Menu>
                                            {chef.departments.map((department) =>
                                                <Dropdown.Item key={department.id}>{department.name}</Dropdown.Item>
                                            )}
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </li>
                                <ButtonToolbar>
                                    <Button variant="light" size="sm"
                                            onClick={() => this.changeChef(chef)}>Change</Button>
                                    <Button variant="danger" size="sm"
                                            onClick={() => {
                                                this.deleteChef(chef)
                                            }}>Delete</Button>
                                </ButtonToolbar>
                            </ul>
                        </ListGroup.Item>
                    )}
                </ListGroup>
                <Button variant="primary" block size="lg" onClick={this.addChef}>Add new chef</Button>
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Input data</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="input-data">
                            <label>First name</label>
                            <input ref={(input) => {
                                this.firstName = input
                            }}
                                   value={this.state.firstName}
                                   onChange={e => this.setState({firstName: e.target.value})}/>
                        </div>
                        <div className="input-data">
                            <label>Middle name</label>
                            <input ref={(input) => {
                                this.middleName = input
                            }}
                                   value={this.state.middleName}
                                   onChange={e => this.setState({middleName: e.target.value})}/>
                        </div>
                        <div className="input-data">
                            <label>Last name</label>
                            <input ref={(input) => {
                                this.lastName = input
                            }}
                                   value={this.state.lastName}
                                   onChange={e => this.setState({lastName: e.target.value})}/>
                        </div>
                        <div className="input-data">
                            <label>Duration work day</label>
                            <input ref={(input) => {
                                this.durationWorkDay = input
                            }}
                                   placeholder="min - 4, max - 10"
                                   value={this.state.durationWorkDay}
                                   onChange={e => this.setState({durationWorkDay: e.target.value})}/>
                        </div>
                        <label>Work shift</label>
                        <ButtonToolbar>
                            <ToggleButtonGroup type="radio" name="options" value={this.state.workShift}
                                               onChange={this.handleChangeWorkShift}>
                                <ToggleButton value="Morning">Morning</ToggleButton>
                                <ToggleButton value="Evening">Evening</ToggleButton>
                            </ToggleButtonGroup>
                        </ButtonToolbar>
                        <label>Operating mode</label>
                        <ButtonToolbar>
                            <ToggleButtonGroup type="radio" name="options" value={this.state.operatingMode}
                                               onChange={this.handleChangeOperatingMode}>
                                <ToggleButton value="5/2">5/2</ToggleButton>
                                <ToggleButton value="2/2">2/2</ToggleButton>
                            </ToggleButtonGroup>
                        </ButtonToolbar>
                        <label>Kitchen</label>
                        <ButtonToolbar>
                            <ToggleButtonGroup type="checkbox" value={this.state.departments}
                                               onChange={this.handleChangeDepartment}>
                                <ToggleButton value="Russian">Russian</ToggleButton>
                                <ToggleButton value="Italian">Italian</ToggleButton>
                                <ToggleButton value="Japanese">Japanese</ToggleButton>
                            </ToggleButtonGroup>
                        </ButtonToolbar>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            Close
                        </Button>
                        <Button variant="primary" onClick={this.sendChefs}>
                            Save
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

export default connect(
    state => ({
        data: state.chef,
    }),
    dispatch => ({
        onGetChefs: (chefs) => {
            dispatch({type: 'ADD_ALL_CHEFS', payload: chefs})
        },
        onAddChef: (chef) => {
            dispatch({type: 'ADD_CHEF', payload: chef})
        },
        onDeleteChef: (id) => {
            dispatch({type: 'DELETE_CHEF', id: id})
        }
    })
)(Chef);
