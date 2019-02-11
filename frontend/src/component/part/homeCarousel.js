import React, {Component} from 'react';
import {Carousel} from 'react-bootstrap';

class HomeCarousel extends Component {
    render() {
        return (
            <div className="home-carousel-component">
                <Carousel>
                    {this.props.pictures.map((picture) =>
                        <Carousel.Item key={picture.id}>
                            <img id="image-carousel" src={picture.src} alt=""/>
                            <Carousel.Caption>
                                <div>
                                    <h1>{picture.title}</h1>
                                </div>
                            </Carousel.Caption>
                        </Carousel.Item>
                    )}
                </Carousel>
            </div>
        )
    }
}

export default HomeCarousel;
