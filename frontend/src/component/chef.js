import React, {Component} from 'react';
import {connect} from 'react-redux'
import axios from 'axios';
import {ListGroup, Button, ButtonToolbar, Dropdown} from 'react-bootstrap'

import Head from './part/head'

class Chef extends Component {
    constructor(props) {
        super(props);
        this.loadChefs = this.loadChefs.bind(this);
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

    render() {
        return (
            <div>
                <Head/>
                <ListGroup>
                    {this.props.data.map((chef) =>
                        <ListGroup.Item key={chef.id}>
                            <ul className="list-chefs">
                                <li>{
                                    chef.lastName + " " +
                                    chef.firstName  + " " +
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
                                                <Dropdown.Item id={department.id}>{department.name}</Dropdown.Item>
                                            )}
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </li>
                                {/*<li className="qualification">*/}
                                    {/*<label>Qualification:</label>*/}
                                    {/*<ul>*/}
                                        {/*{chef.departments.map((department) =>*/}
                                            {/*<li>{department.name}</li>*/}
                                        {/*)}*/}
                                    {/*</ul>*/}
                                {/*</li>*/}
                                <ButtonToolbar>
                                    <Button variant="light" size="sm">Change</Button>
                                    <Button variant="danger" size="sm">Delete</Button>
                                </ButtonToolbar>
                            </ul>
                        </ListGroup.Item>
                    )}
                </ListGroup>
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
            dispatch({type: 'ADD_ALL', payload: chefs})
        },
    })
)(Chef);
