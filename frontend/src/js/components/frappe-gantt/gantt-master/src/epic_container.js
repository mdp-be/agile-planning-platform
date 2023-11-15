import {$, createSVG, animateSVG} from './svg_utils';

export default class Epic_container {

    constructor(gantt, task) {
        this.set_defaults(gantt, task);
        this.prepare();
        this.draw();
        // this.draw_header();
        // this.bind();
    }

    set_defaults(gantt, task) {
        this.gantt = gantt;
        this.task = task;
    }

    prepare() {
        this.prepare_values();
        this.prepare_helpers();
    }

    prepare_values() {
        this.height = this.gantt.options.bar_height + this.gantt.options.padding;
        this.x = 0;
        this.y = this.compute_y();
        this.corner_radius = 0;//this.gantt.options.bar_corner_radius;
        this.width = this.gantt.options.epic_container_width;
        this.group = createSVG('g', {
            class: 'epic-container-wrapper ' + (this.task.custom_class || ''),
            'data-id': this.task.id,
        });
        this.epic_container_group = createSVG('g', {
            class: 'epic-container-group',
            append_to: this.group,
        });
    }

    compute_y() {
        return (
            this.gantt.options.header_height + this.gantt.options.padding / 2 + this.task._index * this.height
        );
    }

    prepare_helpers() {
        SVGElement.prototype.getX = function () {
            return +this.getAttribute('x');
        };
        SVGElement.prototype.getY = function () {
            return +this.getAttribute('y');
        };
        SVGElement.prototype.getWidth = function () {
            return +this.getAttribute('width');
        };
        SVGElement.prototype.getHeight = function () {
            return +this.getAttribute('height');
        };
        SVGElement.prototype.getEndX = function () {
            return this.getX() + this.getWidth();
        };
    }

    draw() {
        this.draw_container();
        this.draw_label();
    }

    // draw_header() {
    //     this.$epic_container = createSVG('rect', {
    //         x: 0,
    //         y: 0,
    //         width: this.width,
    //         height: this.gantt.options.header_height + this.gantt.options.padding,
    //         rx: this.corner_radius,
    //         ry: this.corner_radius,
    //         class: 'epic_container-header',
    //         append_to: this.epic_container_group,
    //     });
    //
    //     animateSVG(this.$epic_container, 'width', 0, this.width);
    //
    //     if (this.invalid) {
    //         this.$epic_container.classList.add('epic-container-invalid');
    //     }
    //
    //     createSVG('text', {
    //         x: 0 + this.width / 2,
    //         y: 0 + (this.gantt.options.header_height + this.gantt.options.padding) / 2,
    //         innerHTML: 'EPICS',
    //         class: 'epic-container-label',
    //         append_to: this.epic_container_group,
    //     });
    //     // labels get BBox in the next tick
    //     requestAnimationFrame(() => this.update_label_position());
    // }

    draw_container() {
        this.$epic_container = createSVG('rect', {
            x: this.x,
            y: this.y,
            width: this.width,
            height: this.height,
            rx: this.corner_radius,
            ry: this.corner_radius,
            class: 'epic_container',
            append_to: this.epic_container_group,
        });

        animateSVG(this.$epic_container, 'width', 0, this.width);

        if (this.invalid) {
            this.$epic_container.classList.add('epic-container-invalid');
        }
    }

    draw_label() {
        // createSVG('img', {
        //     x: this.x + this.width / 2,
        //     y: this.y + this.height / 2,
        //     src: base_url + "/images/icons/issuetypes/epic.svg",
        //     class: 'epic-container-label-img',
        //     append_to: this.epic_container_group,
        // })
        createSVG('text', {
            x: this.x + this.width / 2,
            y: this.y + this.height / 2,
            innerHTML: !this.task.epic ? "No Epic" : "<a href=" + base_url + "/browse/" + this.task.epic + ">" + this.task.epic + "</a>",
            class: 'epic-container-label',
            append_to: this.epic_container_group,
        });
        // labels get BBox in the next tick
        requestAnimationFrame(() => this.update_label_position());
    }

    update_label_position() {
        const epicContainer = this.$epic_container,
            label = this.group.querySelector('.epic-container-label');

        if (label.getBBox().width > epicContainer.getWidth()) {
            label.classList.add('big');
            label.setAttribute('x', epicContainer.getX() + epicContainer.getWidth() + 5);
        } else {
            label.classList.remove('big');
            label.setAttribute('x', epicContainer.getX() + epicContainer.getWidth() / 2);
        }
    }

}