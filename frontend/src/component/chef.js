import React, {Component} from 'react';
import {connect} from 'react-redux'
import axios from 'axios';
import {
    ListGroup,
    Button,
    ButtonToolbar,
    Dropdown,
    Modal,
    ButtonGroup,
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
        this.state = {
            show: false,
            departments: [],
            operatingMode: "5/2",
            workShift: "Morning",
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

    sendChefs() {
        const chef = {};
        if (!isNumeric(this.durationWorkDay)) {
            return;
        }
        chef.firstName = this.firstName;
        chef.middleName = this.middleName;
        chef.lastName = this.lastName;
        chef.durationWorkDay = this.durationWorkDay;
        chef.departments = [];
        for (let i = 0; i < this.state.departments.length; i++) {
            chef.departments.push(this.state.departments[i]);
        }
        chef.workShift = this.state.workShift;
        chef.operatingMode = {};
        const str = this.state.operatingMode.split("/");
        chef.operatingMode.countWorkingDay = str[0];
        chef.operatingMode.countDayOff = str[1];
        axios.post('/chef/add', chef)
            .then(res => {
                if (typeof res.data.error === "undefined"){
                    alert("Data was successfully loaded");
                } else {
                    alert(res.data.error + " Repeat please")
                }
            })
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
                                    <Button variant="light" size="sm">Change</Button>
                                    <Button variant="danger" size="sm">Delete</Button>
                                </ButtonToolbar>
                            </ul>
                        </ListGroup.Item>
                    )}
                </ListGroup>
                <Button variant="primary" block size="lg" onClick={this.handleShow}>Add new chef</Button>
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Input data</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="input-data">
                            <label>First name</label>
                            <input ref={(input) => {
                                this.firstName = input
                            }}/>
                        </div>
                        <div className="input-data">
                            <label>Middle name</label>
                            <input ref={(input) => {
                                this.middleName = input
                            }}/>
                        </div>
                        <div className="input-data">
                            <label>Last name</label>
                            <input ref={(input) => {
                                this.lastName = input
                            }}/>
                        </div>
                        <div className="input-data">
                            <label>Duration work day</label>
                            <input ref={(input) => {
                                this.durationWorkDay = input
                            }} placeholder="min - 4, max - 10"/>
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
                        <Button variant="primary" onClick={this.handleClose}>
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
    })
)(Chef);
